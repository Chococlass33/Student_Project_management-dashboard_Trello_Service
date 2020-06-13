import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class Member
{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private String id;
    private String memberType;
    private String fullName;
    private String email;
    private Timestamp dateCreated;
    private Timestamp dateLastModified;

    protected Member(){}

    public Member(String id, String memberType, String fullName, String email, Timestamp dateCreated, Timestamp dateLastModified)
    {
        this.id = id;
        this.memberType = memberType;
        this.fullName = fullName;
        this.email = email;
        this.dateCreated = dateCreated;
        this.dateLastModified = dateLastModified;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getMemberType()
    {
        return memberType;
    }

    public void setMemberType(String memberType)
    {
        this.memberType = memberType;
    }

    public String getFullName()
    {
        return fullName;
    }

    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
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
