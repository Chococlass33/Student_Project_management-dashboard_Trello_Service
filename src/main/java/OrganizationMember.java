import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class OrganizationMember
{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private String id;
    private String idOrganisation;
    private String idMember;
    private Timestamp dateCreated;
    private Timestamp dateLastModified;

    public OrganizationMember(String id, String idOrganisation, String idMember, Timestamp dateCreated, Timestamp dateLastModified)
    {

        this.id = id;
        this.idOrganisation = idOrganisation;
        this.idMember = idMember;
        this.dateCreated = dateCreated;
        this.dateLastModified = dateLastModified;
    }

    protected OrganizationMember(){}


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

    public String getIdMember()
    {
        return idMember;
    }

    public void setIdMember(String idMember)
    {
        this.idMember = idMember;
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
