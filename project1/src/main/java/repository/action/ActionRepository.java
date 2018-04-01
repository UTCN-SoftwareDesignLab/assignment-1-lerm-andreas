package repository.action;

import model.Action;
import repository.EntityNotFoundException;

import java.util.List;

public interface ActionRepository {

    public boolean saveAction(Action action);

    public List<Action> findActionsForUser(Long userId) throws EntityNotFoundException;

}
