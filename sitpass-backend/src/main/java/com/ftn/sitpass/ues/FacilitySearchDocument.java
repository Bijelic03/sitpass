package com.ftn.sitpass.ues;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(indexName = "facilities")
public class FacilitySearchDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Keyword)
    private String nameSort;

    @Field(type = FieldType.Text)
    private String nameNormalized;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Text)
    private String descriptionNormalized;

    @Field(type = FieldType.Text)
    private String pdfDescription;

    @Field(type = FieldType.Text)
    private String pdfDescriptionNormalized;

    @Field(type = FieldType.Keyword)
    private String city;

    @Field(type = FieldType.Keyword)
    private List<String> disciplines;

    @Field(type = FieldType.Integer)
    private Integer reviewCount;

    @Field(type = FieldType.Double)
    private Double avgStaff;

    @Field(type = FieldType.Double)
    private Double avgEquipment;

    @Field(type = FieldType.Double)
    private Double avgHygiene;

    @Field(type = FieldType.Double)
    private Double avgSpace;

    @Field(type = FieldType.Keyword)
    private String createdAt;
}
