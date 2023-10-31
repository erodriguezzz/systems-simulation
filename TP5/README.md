# Vibrating Silo

## Execution
Remember to run the python script `particles_generator.py` before running any simulation.
You should be in `/TP5/src/main/python` when running the script.

## Input
You can change the configuration parameters in the file `config.json`. The parameters are:
```json
{
    "L": // Length of the silo,
    "M": // Height of the silo,
    "Gamma": //,
    "Mass": // Mass of the particles,
    "Mu": // Friction coefficient,
    "Kn": // Normal stiffness,
    "Rmin": // Minimum radius of the particles (cm),
    "Rmax": // Maximum radius of the particles (cm),
    "A": // Amplitude of the vibration,
    "G": // Acceleration of gravity,
    "iterationPerFrame": // Number of iterations per frame,
    "Ds": // Array of possible diameter of the silo,
    "dts": // Array of possible time steps,
    "Ws": // Array of possible angular velocities,
    "Ns": // Array of possible number of particles,
}
```
It is important to mention that every value should be a `double` and the arrays should be of type `Array<double>`. The program will run for every possible combination of the values in the arrays.