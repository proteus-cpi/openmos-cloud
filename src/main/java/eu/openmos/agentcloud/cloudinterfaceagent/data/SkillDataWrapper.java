package eu.openmos.agentcloud.cloudinterfaceagent.data;

import eu.openmos.model.Base;
import eu.openmos.model.Skill;
import java.io.Serializable;

public class SkillDataWrapper extends Base implements Serializable {
    private static final long serialVersionUID = 6529685098267757720L;      
    
    private Skill skill;    
    private String skillId;

    /**
     * The ontology requested.
     */
    private String ontology;
    
    private String message;

    public Skill getSkill() {
        return skill;
    }

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOntology() {
        return ontology;
    }

    public void setOntology(String ontology) {
        this.ontology = ontology;
    }
}
