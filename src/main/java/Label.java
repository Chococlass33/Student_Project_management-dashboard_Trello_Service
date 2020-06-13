import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class Label
{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private String id;
    private String idBoard;
    private String name;
    private String colour;
    private Timestamp dateCreated;
    private Timestamp dateLastModified;

    public Label(String id, String idBoard, String name, String colour, Timestamp dateCreated, Timestamp dateLastModified)
    {
        this.id = id;
        this.idBoard = idBoard;
        this.name = name;
        this.colour = colour;
        this.dateCreated = dateCreated;
        this.dateLastModified = dateLastModified;
    }

    protected Label(){}

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getIdBoard()
    {
        return idBoard;
    }

    public void setIdBoard(String idBoard)
    {
        this.idBoard = idBoard;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getColour()
    {
        return colour;
    }

    public void setColour(String colour)
    {
        this.colour = colour;
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
