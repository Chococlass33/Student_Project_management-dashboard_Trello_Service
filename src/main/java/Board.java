import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class Board
{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private String id;
    private String idOrganisation;
    private String name;
    private String desc;
    private String descData;
    private String shortLink;
    private Timestamp dateCreated;
    private Timestamp dateLastModified;

    public Board(String id, String idOrganisation, String name, String desc, String descData, String shortLink, Timestamp dateCreated, Timestamp dateLastModified)
    {
        this.id = id;
        this.idOrganisation = idOrganisation;
        this.name = name;
        this.desc = desc;
        this.descData = descData;
        this.shortLink = shortLink;
        this.dateCreated = dateCreated;
        this.dateLastModified = dateLastModified;
    }

    protected Board(){}

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getIdOrganisation()
    {
        return idOrganisation;
    }

    public void setIdOrganisation(String idOrganisation)
    {
        this.idOrganisation = idOrganisation;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
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

    public String getShortLink()
    {
        return shortLink;
    }

    public void setShortLink(String shortLink)
    {
        this.shortLink = shortLink;
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
