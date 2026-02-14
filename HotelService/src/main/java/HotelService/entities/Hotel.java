package HotelService.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collections;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "hotels")
public class Hotel {
    @Id
    private  String id;
    private  String name;
    private  String location;
    private  String about;
}
