### Photo Exhibition Backend Overview ###

This repository contains the backend for the Photo Exhibition project, organized into four main layers: Controller, Service, Entity, and Repository.

1.	Controller layer
    - PhotoController.java: Provides API endpoints for photo-related operations
      
2.	Service layer
    - PhotoService.java: handles photo-related business logic, such as measuring image dimensions and saving photo information to the database
    - CityService.java: Manages city-related business logic, such as retrieving regions
    - GoogleDriveService.java: Manages Google Drive interactions, including downloading images and creating thumbnails
      
3.	Entities
    -	Photo.java: Represents photo information
    -	City.java: Represents city information
      
4.	Repository
    -	PhotoRepository.java: Handles data access and queries for the Photo entity
    -	CityRepository.java: Handles data access and queries for the City entity
  
![Image](https://github.com/user-attachments/assets/1bf04c12-1637-497d-81fd-552a0484b15a)
