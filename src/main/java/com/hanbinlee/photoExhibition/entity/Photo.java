package com.hanbinlee.photoExhibition.entity;

import jakarta.persistence.*;

import java.util.Optional;


@Entity
@Table(name = "photos")
public class Photo {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
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

    @Column(name = "image_url", nullable = false)
    private String imageUrl;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId(){
        return id;
    }

    public void setRegion(String region) {
        this.region = region;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }


}
