import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class CardLabel
{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private String id;
    private String idCard;
    private String idLabel;
    private Timestamp dateCreated;
    private Timestamp dateLastModified;

    public CardLabel(String id, String idCard, String idLabel, Timestamp dateCreated, Timestamp dateLastModified)
    {

        this.id = id;
        this.idCard = idCard;
        this.idLabel = idLabel;
        this.dateCreated = dateCreated;
        this.dateLastModified = dateLastModified;
    }

    protected CardLabel(){}

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

    public String getIdLabel()
    {
        return idLabel;
    }

    public void setIdLabel(String idLabel)
    {
        this.idLabel = idLabel;
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
