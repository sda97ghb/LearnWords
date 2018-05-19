package com.divanoapps.learnwords.data.wombat;

import com.divanoapps.learnwords.data.Repository;
import com.divanoapps.learnwords.data.Specification;
import com.divanoapps.learnwords.data.local.Sync;
import com.divanoapps.learnwords.data.local.Syncable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by dmitry on 14.05.18.
 */

public class CollectionSync<E extends Syncable, I extends Id> {
    enum Have {
        ONLY_SERVER_HAS,
        ONLY_CLIENT_HAS,
        BOTH_HAVE
    }

    enum Newer {
        CLIENT_VERSION_IS_NEWER,
        SERVER_VERSION_IS_NEWER,
        SAME
    }

    enum SyncFlag {
        NULL,
        ADDED,
        DELETED
    }

    private Class<E> klass;
    private Repository<E> repository;
    private Specification getAllSpecification;
    private GetByIdSpecificationFactory<I> getByIdSpecificationFactory;
    private EntityToIdConverter<E, I> entityToIdConverter;
    private NetworkInteractor<E, I> networkInteractor;
    private ArrayFactory<E> arrayFactory;

    public CollectionSync(Class<E> klass, Repository<E> repository, Specification getAllSpecification,
                          GetByIdSpecificationFactory<I> getByIdSpecificationFactory,
                          EntityToIdConverter<E, I> entityToIdConverter, NetworkInteractor<E, I> networkInteractor,
                          ArrayFactory<E> arrayFactory) {
        this.klass = klass;
        this.repository = repository;
        this.getAllSpecification = getAllSpecification;
        this.getByIdSpecificationFactory = getByIdSpecificationFactory;
        this.entityToIdConverter = entityToIdConverter;
        this.networkInteractor = networkInteractor;
        this.arrayFactory = arrayFactory;
    }

    interface Predicate<G> { boolean check(G obj); }
    private <G> List<G> filter(List<G> list, Predicate<G> predicate) {
        List<G> filtered = new LinkedList<>();
        for (G obj : list)
            if (predicate.check(obj))
                filtered.add(obj);
        return filtered;
    }

    private List<Remote<I>> getRemotes(String entity) {
        return networkInteractor.getRemotes(entity);
    }

    private E download(I id) {
        return networkInteractor.download(id);
    }

    private void upload(I id) {
        E obj = repository.query(getByIdSpecificationFactory.create(id)).get(0);
        networkInteractor.upload(obj);
    }

    private void setSyncToNull(I id) {
        E obj = repository.query(getByIdSpecificationFactory.create(id)).get(0);
        obj.setSync(null);
        repository.update(obj);
    }

    private void completelyDeleteOnClient(I id) {
        E obj = repository.query(getByIdSpecificationFactory.create(id)).get(0);
        repository.delete(arrayFactory.singleton(obj));
    }

    private void deleteOnServer(I id) {
        networkInteractor.deleteOnServer(id);
    }

    public void execute() {
        List<E> localEntities = repository.query(getAllSpecification);
        LocalFactory<E, I> localFactory = new LocalFactory<>(entityToIdConverter);
        List<Local<I>> locals = new LinkedList<>();
        for (E entity : localEntities)
            locals.add(localFactory.create(entity));

        List<Remote<I>> remotes = getRemotes(klass.getSimpleName());

        Map<I, Have> haves = new HashMap<>();
        Map<I, Newer> newers = new HashMap<>();
        Map<I, SyncFlag> syncs = new HashMap<>();

        List<I> localIds = new LinkedList<>();
        for (Local<I> local : locals)
            localIds.add(local.getId());

        List<I> remoteIds = new LinkedList<>();
        for (Remote<I> remote : remotes)
            remoteIds.add(remote.getId());

        Set<I> allIds = new HashSet<>();
        allIds.addAll(localIds);
        allIds.addAll(remoteIds);

        for (I id : allIds) {
            boolean clientHas = localIds.contains(id);
            boolean serverHas = remoteIds.contains(id);
            if (clientHas && serverHas)
                haves.put(id, Have.BOTH_HAVE);
            else if (clientHas)
                haves.put(id, Have.ONLY_CLIENT_HAS);
            else
                haves.put(id, Have.ONLY_SERVER_HAS);
        }

        for (I id : allIds) {
            List<Local<I>> localFilter = filter(locals, local -> local.getId().equals(id));
            if (localFilter.isEmpty()) {
                newers.put(id, Newer.SERVER_VERSION_IS_NEWER);
                continue;
            }
            List<Remote<I>> remoteFilter = filter(remotes, remote -> remote.getId().equals(id));
            if (remoteFilter.isEmpty()) {
                newers.put(id, Newer.CLIENT_VERSION_IS_NEWER);
                continue;
            }
            long localTimestamp = localFilter.get(0).getTimestamp();
            long remoteTimestamp = remoteFilter.get(0).getTimestamp();
            if (localTimestamp < remoteTimestamp)
                newers.put(id, Newer.SERVER_VERSION_IS_NEWER);
            else if (localTimestamp > remoteTimestamp)
                newers.put(id, Newer.CLIENT_VERSION_IS_NEWER);
            else
                newers.put(id, Newer.SAME);
        }

        for (I id : allIds) {
            List<Local<I>> localFilter = filter(locals, local -> local.getId().equals(id));
            if (localFilter.isEmpty())
                syncs.put(id, SyncFlag.NULL);
            else if (localFilter.get(0).getSync() == Sync.ADD)
                syncs.put(id, SyncFlag.ADDED);
            else if (localFilter.get(0).getSync() == Sync.DELETE)
                syncs.put(id, SyncFlag.DELETED);
            else
                syncs.put(id, SyncFlag.NULL);
        }

        for (I id : allIds) {
            Have have = haves.get(id);
            Newer newer = newers.get(id);
            SyncFlag syncFlag = syncs.get(id);

            if (have == Have.ONLY_SERVER_HAS) {
                // Download
                E obj = download(id);
                // TODO: fix this shitcode
                if (obj == null)
                    continue;
                // Set sync to null
                obj.setSync(null);
                // Insert
                repository.insert(arrayFactory.singleton(obj));
            }
            else if (have == Have.ONLY_CLIENT_HAS) {
                if (syncFlag == SyncFlag.ADDED) {
                    // Upload
                    upload(id);
                    // Set sync to null
                    setSyncToNull(null);
                }
                else {
                    // Completely delete on client
                    completelyDeleteOnClient(id);
                }
            }
            else {
                if (syncFlag == SyncFlag.ADDED) {
                    if (newer == Newer.CLIENT_VERSION_IS_NEWER) {
                        // Upload
                        upload(id);
                        // Set sync to null
                        setSyncToNull(id);
                    }
                    else if (newer == Newer.SERVER_VERSION_IS_NEWER) {
                        // Download
                        E obj = download(id);
                        // Set sync to null
                        obj.setSync(null);
                        // Update
                        repository.update(obj);
                    }
                    else {
                        // Upload
                        upload(id);
                        // Set sync to null
                        setSyncToNull(id);
                    }
                }
                else if (syncFlag == SyncFlag.DELETED) {
                    if (newer == Newer.CLIENT_VERSION_IS_NEWER) {
                        // Completely delete on client
                        completelyDeleteOnClient(id);
                        // Delete on server
                        deleteOnServer(id);
                    }
                    else if (newer == Newer.SERVER_VERSION_IS_NEWER) {
                        // Download
                        E obj = download(id);
                        // Set sync to null
                        obj.setSync(null);
                        // Update
                        repository.update(obj);
                    }
                    else {
                        // Completely delete on client
                        completelyDeleteOnClient(id);
                        // Delete on server
                        deleteOnServer(id);
                    }
                }
                else {
                    if (newer == Newer.CLIENT_VERSION_IS_NEWER) {
                        // Upload
                        upload(id);
                    }
                    else if (newer == Newer.SERVER_VERSION_IS_NEWER) {
                        // Download
                        E obj = download(id);
                        // Update
                        repository.update(obj);
                    }
                    else {
                        // nothing
                    }
                }
            }
        }
    }
}
