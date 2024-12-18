
# Mandatory Task

### Stack:
- Java 17 Or Kotlin 1.8
- Spring-boot 2.7+ or 3+
  
### Scenario:
- You are developing a system that takes a file (defined below) and processing it to
  create an &#39;Outcome file&#39;.
- You must use REST to trigger the application, this can be done by sending the
  initial file using HTTP POST.
- You must parse the file and pull the details needed to create the &#39;Outcome File&#39;.
  o Please note, validation of the file is important for this activity.
- Once the end file had been created, it needs to be passed back to the user.
- There should be a feature flag added to skip the validation.
  
### Notes:
- In this activity we will be looking into your ways of thinking and choices of
  actions therefore it is NOT important to complete all the points listed in the scenario
  section.
- We will be looking at your familiarity with spring boot, java/kotlin and overall
  theory.
  o Specifically, the SOLID principles.
  
### Initial file:
  - Name: EntryFile.txt
  - Type: TXT
  - Content:
  18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1
  \n
  3ce2d17b-e66a-4c1e-bca3-40eb1c9222c7|2X2D24|Mike Smith|Likes Grape|Drives an SUV|35.0|95.5
  \n
  1afb6f5d-a7c2-4311-a92d-974f3180ff5e|3X3D35|Jenny Walters|Likes Avocados|Rides A
  Scooter|8.5|15.3

- Content Structure:
UUID, ID, Name, Likes, Transport, Avg Speed, Top Speed
- OutcomeFile:
  - Type: JSON
  - Name: OutcomeFile.json
- Content Structure:
Name, Transport, Top Speed

