package com.hanbinlee.photoExhibition.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "photos")
public class Photo {
    @ManyToOne
    @JoinColumn(name = "city_id")
    @JsonBackReference
    private City city;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "region", nullable = false)
    private String region;
    @Column(name = "sub_Region", nullable = false)
    private String subRegion;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "origin_file_Path", nullable = false)
    private String originFilePath;
    @Column(name = "thumbnail_file_Path", nullable = false)
    private String thumbnailFilePath;

    @Column(name ="width", nullable = false)
    private int width;
    @Column(name ="height", nullable = false)
    private int height;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId(){
        return id;
    }

    public void setRegion(String region) {
        this.region = region;
    }
    public String getRegion(){
        return region;
    }
    public String getSubRegion() {
        return subRegion;
    }

    public void setSubRegion(String subRegion) {
        this.subRegion = subRegion;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getOriginFilePath(){
        return originFilePath;
    }

    public void setOriginFilePath(String originFilePath) {
        this.originFilePath = originFilePath;
    }
    
    public String getThumbnailFilePath(){
        return thumbnailFilePath;
    }

    public void setThumbnailFilePath(String thumbFilePath) {
        this.thumbnailFilePath = thumbFilePath;
    }
    
    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public void setWidth(int width){ this.width = width; }
    public int getWidth(){ return width; }
    public void setHeight(int height){ this.height = height; }
    public int getHeight(){ return height; }

}
