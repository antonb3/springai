package feeltheagi.springai.repo;

import feeltheagi.springai.model.Agent;

import java.util.List;

public interface AgentRepo {

    public List<Agent> getAll();

    public Agent getOneByName(String name);

    public void add(Agent agent);
}
