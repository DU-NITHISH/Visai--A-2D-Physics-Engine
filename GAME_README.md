# Jump Ball Adventure - Game with Database Integration

## Overview
A 2D platformer game with **3 levels**, **SQL database integration**, **player tracking**, **timer system**, and **leaderboard**.

## Features

### ✨ New Features Added
1. **SQLite Database Integration**
   - Player records stored in local database (`jumpball.db`)
   - Automatic table creation on first run
   - Persistent leaderboard data

2. **Player Name Entry System**
   - Name required before starting game
   - Validation to ensure name is not empty

3. **Timer System**
   - Timer starts when "Start Game" is clicked
   - Runs continuously across all 3 levels
   - Stops when all levels are completed
   - Time displayed in format: MM:SS.mmm

4. **Leaderboard System**
   - Displays top 10 fastest completions
   - Shown in two locations:
     - **Menu Page**: Before starting game
     - **Congratulations Dialog**: After completing all levels
   - Highlights top 3 players (Gold, Silver, Bronze)
   - Current player's record highlighted in yellow

5. **Level 3 - Final Challenge**
   - **High Difficulty**: Complex platform layout with narrow jumps
   - **Moving Platforms**: 3 moving platforms that require precise timing
   - **Shooting Enemies**: 5 enemies that fire projectiles at the player
   - **Multiple Hazards**: Spikes, gaps in the ground, and deadly projectiles
   - **Dark Atmospheric Theme**: Purple/dark background for intense feel

### 🎮 Game Progression
1. **Level 1**: Basic platforming with moving enemies
2. **Level 2 (Boss Level)**: Boss fight - jump on boss to damage
3. **Level 3**: Final challenge with shooting enemies and moving platforms
4. **Completion**: Congratulations popup with time and leaderboard

## How to Play

### Controls
- **Move Left**: `A` or `←` (Left Arrow)
- **Move Right**: `D` or `→` (Right Arrow)
- **Jump**: `W`, `↑` (Up Arrow), or `Space`
- **Restart Level**: `R`

### Game Flow
1. Launch the game
2. View leaderboard on menu page
3. Click "Start Game"
4. Enter your player name (required)
5. Timer starts automatically
6. Complete all 3 levels
7. View your completion time and updated leaderboard
8. Return to menu to play again

## Technical Details

### Database Schema
```sql
CREATE TABLE players (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    completion_time_ms BIGINT NOT NULL,
    completed_date TEXT NOT NULL
)
```

### Files Added/Modified

#### New Files:
- `DatabaseManager.java` - Database operations (create, insert, query)
- `Level3.java` - Final level with high difficulty
- `CongratsDialog.java` - Congratulations popup with leaderboard
- `compile_and_run.sh` - Build and run script
- `sqlite-jdbc.jar` - SQLite JDBC driver

#### Modified Files:
- `JumpBallAdventure.java` - Added timer, player name, database integration
- `MenuPage.java` - Added leaderboard display
- `Level1.java` - Updated level progression
- `BossLevel.java` - Updated to progress to Level 3

### Dependencies
- **Java JDK 11 or newer**
- **SQLite JDBC Driver**: v3.47.1.0 (included as `sqlite-jdbc.jar`)

## Running the Game

### Option 1: Using the Shell Script
```bash
cd /app
./compile_and_run.sh
```

### Option 2: Manual Compilation
```bash
# Compile
javac -d /app/build -cp "/app/sqlite-jdbc.jar" \
  /app/Physics2D/*.java \
  /app/JumpBallGame/*.java

# Run
java -cp "/app/build:/app/sqlite-jdbc.jar" JumpBallGame.JumpBallAdventure
```

## Database Location
- The SQLite database file `jumpball.db` is created in the directory where you run the game
- Leaderboard data persists between game sessions
- To reset leaderboard, delete `jumpball.db`

## Level 3 Features in Detail

### Shooting Enemies
- **5 strategically placed enemies** that fire projectiles
- Each enemy has different shooting intervals
- Projectiles track player's position when fired
- Must avoid projectiles while navigating platforms

### Moving Platforms
- **3 horizontal moving platforms**
- Different movement ranges and speeds
- Timing jumps is crucial

### Enhanced Difficulty
- Multiple spikes and hazards
- Gaps in the ground (no continuous floor)
- Narrow platforms requiring precise jumps
- Combination of static and dynamic challenges

## Leaderboard Features
- **Top 10 Rankings**: Shows fastest completion times
- **Color-coded medals**:
  - 🥇 Gold: 1st place
  - 🥈 Silver: 2nd place
  - 🥉 Bronze: 3rd place
- **Highlight system**: Current player's latest run highlighted
- **Time format**: MM:SS.mmm (minutes:seconds.milliseconds)

## Troubleshooting

### Game won't start
- Ensure Java JDK 11+ is installed: `java -version`
- Check that `sqlite-jdbc.jar` is in `/app` directory

### Database errors
- Ensure write permissions in the directory
- Delete `jumpball.db` and restart to recreate database

### Display issues
- Game requires graphical display (X11 or similar)
- May not work in headless environments

## Future Enhancements (Ideas)
- More levels with increasing difficulty
- Power-ups and collectibles
- Sound effects for different actions
- Multiplayer mode
- Custom level editor
- Achievement system
- Daily/Weekly challenges

## Credits
- Built on top of Visai 2D Physics Engine
- Database: SQLite
- UI: Java Swing

---

**Enjoy the game and try to get the #1 spot on the leaderboard!** 🏆
