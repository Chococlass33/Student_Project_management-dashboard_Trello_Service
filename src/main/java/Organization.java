import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class Organization
{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private String id;
    private String name;
    private String displayName;
    private String desc;
    private String descData;
    private String teamType;
    private Timestamp dateCreated;
    private Timestamp dateLastModified;

    public Organization(String id, String name, String displayName, String desc, String descData, String teamType, Timestamp dateCreated, Timestamp dateLastModified)
    {

        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.desc = desc;
        this.descData = descData;
        this.teamType = teamType;
        this.dateCreated = dateCreated;
        this.dateLastModified = dateLastModified;
    }
    protected Organization(){}


    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    public String getDescData()
    {
        return descData;
    }

    public void setDescData(String descData)
    {
        this.descData = descData;
    }

    public String getTeamType()
    {
        return teamType;
    }

    public void setTeamType(String teamType)
    {
        this.teamType = teamType;
    }

    public Timestamp getDateCreated()
    {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated)
    {
        this.dateCreated = dateCreated;
    }

    public Timestamp getDateLastModified()
    {
        return dateLastModified;
    }

    public void setDateLastModified(Timestamp dateLastModified)
    {
        this.dateLastModified = dateLastModified;
    }
}
