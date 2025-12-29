# 🧩 Rubik’s Cube Solver

A Rubik’s Cube Solver built using **Java**, **Graph Algorithms**, and a **web-based frontend**.
The project demonstrates how classic search algorithms can be applied to a complex state-space problem using clean Object-Oriented design and a simple client–server architecture.

---

## 🔧 Tech Stack

**Backend**
- Java
- Built-in Java HTTP Server
- Object-Oriented Programming(OOP)

**Frontend**
- HTML
- CSS
- JavaScript (Fetch API)

**Algorithms**
- Breadth-First Search (BFS)
- A* Search(heuristic-based demo)
- Kociemba Algorithm (Demo)

---

## ⚙️ How It Works
1. User enters a cube scramble and selects an algorithm
2. Frontend sends data to backend via HTTP POST
3. Backend models the cube as a state-space graph
4. Selected algorithm computes the solution
5. Move sequence is returned and displayed on the UI 

---

## 🧠 Algorithms Overview

### BFS (Breadth-First Search)
- Explores states level by level  
- Finds shortest path within depth limit  
- High memory usage  

### A* Search
- Uses heuristic-based informed search  
- Demonstrates AI-inspired decision making
- Implemented as a heuristic demo  

### Kociemba (Demo)
- Industry-standard two-phase solving approach  
- Implemented here as a demo solver for optimal solutions
