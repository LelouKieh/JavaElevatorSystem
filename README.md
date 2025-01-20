# About/Overview
This project is a simulation of a building elevator system. The program models a building with a specified number of floors and elevators, each with a certain capacity. The system can handle requests for elevators, move elevators between floors, and keep track of the status of each elevator. The program is written in Java and uses the MVC (Model-View-Controller) design pattern.

# List of Features
- Create a building with a specified number of floors, elevators
- Set the capacity for each elevator
- Add requests for the building and distribute them to elevators
- Start and stop the elevator system
- Step through the elevator system
- Display the current status of the elevator system with each elevator

# How To Run
To run the program, you need to execute the jar file with the following command:
java -jar BuildingElevatorSystem.jar \<floors\> \<elevators\> \<capacity\>
Where:  
- \<floors\> is the number of floors in the building
- \<elevators\> is the number of elevators in the building
- \<capacity\> is the capacity of each elevator

You can also use this command without arguments to run the program, and the program will ask you to set the floors, number of elevators and capacity on a start menu:
java -jar BuildingElevatorSystem.jar

Or you can just double click the jar file. Set the number of floors, number of elevators and elevator capacity with the slides, then click set building to run the program.

# How to Use the Program
On the start window:
- Use the three slides to set the number of floors, the number of elevators and each elevator’s capacity.
- Press the set building button to set a building with the data from the slides.
On the main window:
- Press the start button to start the building elevator systems.
- Press the stop button to stop the system.
- Press the step button to step the system.
- Enter int for start floor and end floor, press the make request button to make a request. If the user input is invalid, show an error message.
The program provides a graphical user interface for interacting with the elevator system. The current status of the system is displayed in the interface.
The colour of the building squares represents:
- RED: the elevator is out of service.
- GREY: the elevator will stop on these floors
- YELLOW: the elevator is on the floor with door closed.
- GREEN: the elevator is on the floor with door open.
The characters on the building squares represents:
- ^: The direction of the elevator is up.
- v: The direction of the elevator is down.
- -: The direction of the elevator is stopped.
- Numbers on the elevator: The timer for waiting countdown.

# Design/Model Changes
No model changes are made to the version of Part 1 Assignment. But some changes are implemented compared to the initial design document, including adding new getters for more fields in the building model to help the controller, and updating the way of distributing requests to the elevators.
- getElevators(): This method is used to get the elevators in the building.
- getRequests(): Get requests for a specific elevator

# Assumptions
- The building has at least 3 floors and one elevator.
- The building has a maximum of 20 elevators.
- The building has a maximum of 30 floors.
- The capacity of each elevator is at least 3, and at most 20.
- The ground floor is the 0 floor.
- All the elevators stay on the 0 floor when the building system is set up for the first time.

# Limitations
- The program does not support multiple buildings or different types of elevators.
- The view is not friendly if the user creates too many floors and elevators.
- The user need to restart the program if they want to set up a new building with different parameters.
- There is no timer and the user need to step manually.

# Citations
- “A visual guide to layout managers,” A Visual Guide to Layout Managers (The JavaTM Tutorials \> Creating a GUI With Swing \> Laying Out Components Within a Container), https://docs.oracle.com/javase/tutorial/uiswing/layout/visual.html (accessed Apr. 20, 2024). 
- “A visual guide to swing components,” A Visual Guide to Swing Components (from: The JavaTM Tutorials \> Graphical User Interfaces \> Swing Features), https://web.mit.edu/6.005/www/sp14/psets/ps4/java-6-tutorial/components.html (accessed Apr. 20, 2024). 
- Copilot.
