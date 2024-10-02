package feeltheagi.springai.repo;

import feeltheagi.springai.model.Agent;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AgentRepoImpl implements AgentRepo {

    private Map<String, Agent> all = new HashMap<>();

    public List<Agent> getAll() {
        List<Agent> list = new ArrayList<>();
        list.addAll(all.values());
        return list;
    }

    public Agent getOneByName(String name) {
        return all.get(name.toLowerCase());
    }

    public void add(Agent agent) {
        this.all.put(agent.getName().toLowerCase(),agent);
    }
}
