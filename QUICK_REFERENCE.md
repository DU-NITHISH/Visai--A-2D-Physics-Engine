# 🎮 Jump Ball Adventure - Quick Reference Card

## 🚀 Quick Start
```bash
cd /app
./compile_and_run.sh
```

## 🎯 Controls
| Action | Keys |
|--------|------|
| Move Left | `A` or `←` |
| Move Right | `D` or `→` |
| Jump | `W`, `↑`, or `Space` |
| Restart Level | `R` |

## 📊 Game Structure
```
Menu → Enter Name → Level 1 → Level 2 (Boss) → Level 3 → Congrats → Menu
                      ↓ Timer starts              ↓ Timer stops
```

## 🏆 Features Overview

### ✅ Database
- **Type**: SQLite (`jumpball.db`)
- **Location**: Current directory
- **Purpose**: Store player times & leaderboard

### ✅ Timer
- **Starts**: When "Start Game" clicked
- **Runs**: Continuously through all 3 levels
- **Stops**: When Level 3 completed
- **Format**: MM:SS.mmm

### ✅ Leaderboard
- **Shows**: Top 10 fastest times
- **Locations**: 
  - Menu page (before game)
  - Congrats dialog (after completion)
- **Ranking**: Gold 🥇 Silver 🥈 Bronze 🥉

### ✅ Levels

#### Level 1: Basic Platformer
- **Theme**: Sky blue, friendly
- **Enemies**: 3 moving enemies
- **Hazards**: Spikes, gaps
- **Special**: Collectible coin

#### Level 2: Boss Fight
- **Theme**: Dark gray, intense
- **Boss**: Large red enemy (5 HP)
- **Attacks**: Projectiles, jump attacks
- **Strategy**: Jump on boss to damage

#### Level 3: Final Challenge ⚠️
- **Theme**: Dark purple, extreme
- **Difficulty**: HIGH
- **Features**:
  - 5 shooting enemies
  - 3 moving platforms
  - Multiple spike zones
  - Gaps in ground
  - Complex vertical layout

## 📁 Key Files

### Game Files
- `JumpBallAdventure.java` - Main game controller
- `DatabaseManager.java` - Database operations
- `Level1.java` - First level
- `BossLevel.java` - Second level (boss)
- `Level3.java` - Final level
- `MenuPage.java` - Start menu
- `CongratsDialog.java` - Completion popup

### Support Files
- `sqlite-jdbc.jar` - Database driver
- `compile_and_run.sh` - Build & run
- `jumpball.db` - Database (created on first run)

## 🗄️ Database Commands

### View leaderboard:
```bash
sqlite3 jumpball.db "SELECT name, completion_time_ms FROM players ORDER BY completion_time_ms LIMIT 10;"
```

### Reset leaderboard:
```bash
rm jumpball.db
```

## 🎨 Visual Guide

### Level Themes
- **Level 1**: 🌤️ Bright, sky blue, green hills
- **Level 2**: 🌑 Dark, gray, menacing
- **Level 3**: 🌌 Dark purple, extreme challenge

### Color Code
- 🔴 **Red Ball**: Player
- 🟤 **Brown**: Platforms
- ⚫ **Dark Gray**: Spikes
- 🟣 **Purple**: Enemies (Level 1)
- 🔴 **Dark Red**: Boss (Level 2)
- 🔴 **Dark Red**: Shooting enemies (Level 3)
- 🟠 **Orange**: Projectiles
- 🟡 **Gold**: Moving platforms
- 🟢 **Green**: Goal/Finish

## ⚠️ Death Conditions
- ☠️ Touching spikes
- ☠️ Touching enemies
- ☠️ Hit by projectiles
- ☠️ Falling off screen
- ☠️ Touching boss from side/below

## 💡 Tips & Strategies

### Level 1
- ✅ Collect coin for practice
- ✅ Time enemy movements
- ✅ Use full jump height for high platforms

### Level 2 (Boss)
- ✅ Jump ON TOP of boss to damage
- ✅ Avoid projectiles by moving constantly
- ✅ Stay mobile during jump attacks
- ✅ 5 hits to defeat boss

### Level 3 (Hardest)
- ✅ Study enemy positions before rushing
- ✅ Time moving platform jumps carefully
- ✅ Watch multiple projectiles at once
- ✅ Take your time - no rush
- ✅ Use gaps in shooting to advance
- ✅ Save progress with R key restarts

## 🏃 Speedrun Tips
1. ⚡ Skip Level 1 coin (not required)
2. ⚡ Learn optimal jump paths
3. ⚡ Defeat boss quickly (aggressive play)
4. ⚡ Memorize Level 3 layout
5. ⚡ Use edge of platforms for longer jumps
6. ⚡ Minimize waiting on moving platforms

## 📈 Progression

### Average Times
- **Beginner**: 6-10 minutes
- **Intermediate**: 4-6 minutes
- **Advanced**: 2-4 minutes
- **Expert**: < 2 minutes

### Skill Levels
- **Novice**: Complete all levels (any time)
- **Competent**: Complete under 5 minutes
- **Skilled**: Complete under 3 minutes
- **Master**: Complete under 2 minutes
- **Legend**: Top 3 on leaderboard

## 🔧 Troubleshooting

### Game won't compile
```bash
# Verify Java installation
java -version
javac -version

# Recompile
cd /app
./compile_only.sh
```

### Database issues
```bash
# Check if file exists
ls -lah jumpball.db

# Reset database
rm jumpball.db
# (Will be recreated on next run)
```

### Can't see game window
- ❌ No X11/display server
- ✅ Need graphical environment

## 📞 Files for Help

### Documentation
- `GAME_README.md` - Full game documentation
- `IMPLEMENTATION_SUMMARY.md` - Technical details
- `TESTING_GUIDE.md` - Complete testing procedures
- `QUICK_REFERENCE.md` - This file

## 🎯 Victory Checklist

- [ ] Enter player name
- [ ] Complete Level 1
- [ ] Defeat Boss (Level 2)
- [ ] Complete Level 3
- [ ] View completion time
- [ ] Check leaderboard ranking
- [ ] Try to beat your time!

## 🏆 Leaderboard Goals

### Personal Goals
- [ ] Complete the game once
- [ ] Complete under 5 minutes
- [ ] Complete under 3 minutes
- [ ] Get 3 different sub-5min times
- [ ] Reach top 10
- [ ] Reach top 5
- [ ] Reach top 3
- [ ] Become #1!

### Challenge Modes (Self-Imposed)
- 🎯 No deaths run
- 🎯 Speedrun (fastest time)
- 🎯 Collect all coins (Level 1)
- 🎯 Never stop moving
- 🎯 Minimal jumps

## 📊 Stats You Can Track

After multiple runs, check your:
- Best time
- Average time
- Total completions
- Deaths per level
- Success rate

```bash
# View your runs
sqlite3 jumpball.db "SELECT * FROM players WHERE name='YourName' ORDER BY completion_time_ms;"
```

---

## 🎮 Ready to Play?
```bash
cd /app && ./compile_and_run.sh
```

**Good luck, and have fun!** 🚀🎯🏆
