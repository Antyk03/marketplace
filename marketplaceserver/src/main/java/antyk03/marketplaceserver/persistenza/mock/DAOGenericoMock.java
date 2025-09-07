package antyk03.marketplaceserver.persistenza.mock;

import antyk03.marketplaceserver.persistenza.DAOException;
import antyk03.marketplaceserver.persistenza.IDAOGenerico;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public class DAOGenericoMock<T> implements IDAOGenerico<T> {

    private final Class<T> persistentClass;

    @SuppressWarnings("unchecked")
    public DAOGenericoMock() {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    protected Class<T> getPersistentClass() {
        return persistentClass;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T makePersistent(T entity) throws DAOException {
        RepositoryMock repositoryMock = RepositoryMock.getInstance();
        repositoryMock.saveOrUpdate(entity);
        return entity;
    }

    @Override
    public void makeTransient(T entity) throws DAOException {
        RepositoryMock repositoryMock = RepositoryMock.getInstance();
        repositoryMock.delete(entity);
    }


    @Override
    public T findById(Long id) throws DAOException {
        RepositoryMock repositoryMock = RepositoryMock.getInstance();
        return repositoryMock.findById(id, persistentClass);
    }

    @Override
    public List<T> findAll() throws DAOException {
        RepositoryMock repositoryMock = RepositoryMock.getInstance();
        return repositoryMock.findAll(persistentClass);
    }



}
