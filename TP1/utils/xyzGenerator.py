def generate_xyz_file(particles, selected_id, neighbors):
    print(f"neighboursss: {neighbors} ")
    with open("output.xyz", "w") as f:
        f.write(f"{len(particles)}\n\n")
        for particle in particles:
            if particle['id'] == selected_id:
                color = "0 255 0"  # Green color for selected particle
                print(f"Selected particle: {particle['neighbors']} ")
            elif particle['id'] in neighbors:
                print(f"neighbors: {particle['id']} ")
                color = "255 0 0"  # Red color for neighbors
            else:
                color = "255 255 0"  # Yellow color for others

            line = f"{particle['x']} {particle['y']} {color} {particle['radius']}\n"
            f.write(line)

def read_input_file(filename, id):
    particles = []
    neighborsR = []
    with open(filename, "r") as f:
        for line in f:
            parts = line.strip().split(';')
            particle_id = int(parts[0])
            particle_radius = float(parts[1])
            x = float(parts[2])
            y = float(parts[3])
            neighbors = [int(n.strip()) for n in parts[4].split(",") if n.strip() != ""]
            particles.append({'id': particle_id, 'radius': particle_radius, 'x': x, 'y': y, 'neighbors': neighbors})
            if (particle_id == id):
                neighborsR = (neighbors)
    return (particles, neighborsR)

if __name__ == "__main__":
    input_filename = "./data/output/vecinos.xyz"  # Replace with your input filename
    selected_particle_id = 72 # Replace with the ID of the particle you want to select
    
    (particles, neighbors) = read_input_file(input_filename, selected_particle_id)
    generate_xyz_file(particles, selected_particle_id, neighbors)
