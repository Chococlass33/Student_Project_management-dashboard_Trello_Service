import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class CardMember
{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private String id;
    private String idCard;
    private String idMember;
    private Timestamp dateCreated;
    private Timestamp dateLastModified;

    public CardMember(String id, String idCard, String idMember, Timestamp dateCreated, Timestamp dateLastModified)
    {

        this.id = id;
        this.idCard = idCard;
        this.idMember = idMember;
        this.dateCreated = dateCreated;
        this.dateLastModified = dateLastModified;
    }

    protected CardMember(){}

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getIdCard()
    {
        return idCard;
    }

    public void setIdCard(String idCard)
    {
        this.idCard = idCard;
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
