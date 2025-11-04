# Testing Guide - Jump Ball Adventure

## Prerequisites
✅ Java JDK 11+ installed  
✅ Graphical display available (X11 or similar)  
✅ All files compiled successfully  

## Quick Start Test

### 1. Compile and Run
```bash
cd /app
./compile_and_run.sh
```

Expected: Game window opens with menu page

## Comprehensive Testing Checklist

### Phase 1: Menu & Database Initialization ✓

**Test 1.1: Initial Launch**
- [ ] Game window appears (900x600)
- [ ] Title "Jump Ball Adventure" displayed
- [ ] "Start Game" button visible
- [ ] Instructions shown
- [ ] Leaderboard section visible
- [ ] Leaderboard shows "No records yet. Be the first!" (first run)

**Test 1.2: Database Creation**
- [ ] Check if `jumpball.db` file created in directory
```bash
ls -lah | grep jumpball.db
```

### Phase 2: Player Name Entry ✓

**Test 2.1: Start Game**
- [ ] Click "Start Game" button
- [ ] Name entry dialog appears
- [ ] Dialog title: "Player Name"
- [ ] Input field is empty and ready

**Test 2.2: Name Validation**
- [ ] Try entering empty name and clicking OK → Error message appears
- [ ] Try clicking Cancel → Returns to menu
- [ ] Enter valid name (e.g., "TestPlayer") → Level 1 starts

### Phase 3: Level 1 - Basic Platformer ✓

**Test 3.1: Game Mechanics**
- [ ] Player (red ball) appears at start position
- [ ] A/D or Arrow keys move the player
- [ ] W/Space/Up arrow makes player jump
- [ ] Player collides with platforms correctly
- [ ] Gravity works (player falls down)

**Test 3.2: Hazards**
- [ ] Touching gray spikes → Player dies, "You Died!" message
- [ ] Touching purple enemies → Player dies
- [ ] Falling off screen → Player dies
- [ ] Press R after death → Level restarts

**Test 3.3: Coin System**
- [ ] Yellow coin visible on platform
- [ ] Collecting coin → Shows "+1 Coin!" message
- [ ] Coin counter updates in top right

**Test 3.4: Level Completion**
- [ ] Reach green "GOAL" at end
- [ ] "Level Complete!" message appears
- [ ] After 1.5 seconds → Boss Level loads

### Phase 4: Level 2 - Boss Fight ✓

**Test 4.1: Boss Appearance**
- [ ] Large dark red boss appears on right side
- [ ] Boss HP bar visible at top (red bar)
- [ ] Boss moves towards player

**Test 4.2: Boss Mechanics**
- [ ] Boss fires orange projectiles
- [ ] Boss performs jump attacks
- [ ] Boss moves left/right chasing player

**Test 4.3: Combat**
- [ ] Jump on top of boss → Boss HP decreases, flash effect
- [ ] Boss HP bar updates
- [ ] Touch boss from side → Player dies
- [ ] Touch projectile → Player dies

**Test 4.4: Boss Defeat**
- [ ] Defeat boss (5 hits on top)
- [ ] "Boss Defeated! You Win!" message
- [ ] Explosion particles appear
- [ ] After 2 seconds → Level 3 loads

### Phase 5: Level 3 - Final Challenge ✓

**Test 5.1: Environment**
- [ ] Dark purple/gray background
- [ ] Title "LEVEL 3 - FINAL CHALLENGE" visible
- [ ] Complex platform layout
- [ ] Gaps in ground floor visible

**Test 5.2: Moving Platforms**
- [ ] 3 yellow/gold moving platforms present
- [ ] Platforms move horizontally
- [ ] Can stand on moving platforms
- [ ] Platforms move player with them

**Test 5.3: Shooting Enemies**
- [ ] 5 dark red enemies with yellow eyes visible
- [ ] Enemies are stationary
- [ ] Enemies fire orange projectiles periodically
- [ ] Projectiles move toward player's position
- [ ] Multiple projectiles can be active simultaneously

**Test 5.4: Hazards**
- [ ] Red spikes in multiple locations
- [ ] Touching spikes → Death
- [ ] Touching enemy → Death
- [ ] Touching projectile → Death
- [ ] Falling in gaps → Death
- [ ] Press R → Level restarts

**Test 5.5: Difficulty Verification**
- [ ] Level requires multiple attempts (high difficulty)
- [ ] Projectiles + moving platforms create challenge
- [ ] Narrow jumps require precision
- [ ] Vertical progression is complex

**Test 5.6: Level Completion**
- [ ] Reach green "FINISH" goal at top right
- [ ] "ALL LEVELS COMPLETE!" message appears
- [ ] After 1.5 seconds → Congratulations dialog appears

### Phase 6: Timer System ✓

**Test 6.1: Timer Start**
- Note: Timer tracking is internal, verify through completion time

**Test 6.2: Timer Continuity**
- [ ] Timer runs through Level 1
- [ ] Timer continues during Level 2
- [ ] Timer continues during Level 3
- [ ] Timer does NOT reset between levels

**Test 6.3: Timer Stop**
- [ ] Timer stops when Level 3 completed
- [ ] Completion time displayed in congrats dialog
- [ ] Time format: MM:SS.mmm (e.g., "02:45.123")

### Phase 7: Congratulations Dialog ✓

**Test 7.1: Dialog Appearance**
- [ ] Dialog appears after Level 3 completion
- [ ] Title: "🎉 CONGRATULATIONS! 🎉"
- [ ] Shows "🎊 ALL LEVELS COMPLETED! 🎊"
- [ ] Player name displayed
- [ ] Completion time prominently shown

**Test 7.2: Leaderboard in Dialog**
- [ ] "🏆 TOP 10 LEADERBOARD 🏆" title visible
- [ ] Current player's record highlighted in yellow
- [ ] Leaderboard scrollable if needed
- [ ] Format: "Rank. Name Time"
- [ ] Top 3 color-coded (Gold, Silver, Bronze)

**Test 7.3: Dialog Actions**
- [ ] "Back to Menu" button visible
- [ ] Clicking button → Returns to menu page
- [ ] Menu page leaderboard now updated with new record

### Phase 8: Database & Leaderboard ✓

**Test 8.1: Record Saving**
- [ ] Complete game multiple times with different names
- [ ] Each completion saves to database
- [ ] Can verify with:
```bash
sqlite3 jumpball.db "SELECT * FROM players ORDER BY completion_time_ms;"
```

**Test 8.2: Leaderboard Sorting**
- [ ] Leaderboard shows fastest times first
- [ ] Correctly ranks by completion time
- [ ] Shows top 10 only (if more than 10 records)

**Test 8.3: Multiple Players**
- [ ] Play with name "Alice" → Record saved
- [ ] Play with name "Bob" → Record saved
- [ ] Both appear on leaderboard
- [ ] Sorted by time, not entry order

**Test 8.4: Same Player Multiple Times**
- [ ] Complete game twice with same name
- [ ] Both records saved separately
- [ ] Both appear on leaderboard
- [ ] Shows best/worst times for same player

**Test 8.5: Persistence**
- [ ] Close game completely
- [ ] Restart game
- [ ] Menu leaderboard still shows previous records
- [ ] Database persisted correctly

### Phase 9: Visual Elements ✓

**Test 9.1: Colors & Theming**
- [ ] Level 1: Sky blue background
- [ ] Level 2: Dark gray background
- [ ] Level 3: Dark purple background
- [ ] Player: Red ball with shadow
- [ ] Platforms: Brown (Level 1), Dark (Level 2), Brown/Gold (Level 3)

**Test 9.2: UI Elements**
- [ ] All text readable
- [ ] Buttons have proper styling
- [ ] Leaderboard panels have borders
- [ ] Top 3 rankings colored (Gold/Silver/Bronze)

**Test 9.3: Animations**
- [ ] Player movement smooth
- [ ] Enemies move smoothly
- [ ] Projectiles travel smoothly
- [ ] Moving platforms animate correctly
- [ ] Boss explosion particles work

### Phase 10: Edge Cases & Error Handling ✓

**Test 10.1: Empty Name Handling**
- [ ] Empty string rejected
- [ ] Spaces-only string rejected
- [ ] Null input handled gracefully

**Test 10.2: Database Access**
- [ ] First run creates database without errors
- [ ] Concurrent access handled (if applicable)
- [ ] Write permissions issues caught

**Test 10.3: Restart Functionality**
- [ ] Press R in Level 1 → Level 1 restarts
- [ ] Press R in Level 2 → Level 2 restarts
- [ ] Press R in Level 3 → Level 3 restarts
- [ ] Timer does NOT restart (continues from game start)

**Test 10.4: Death Scenarios**
- [ ] Death by spike
- [ ] Death by enemy contact
- [ ] Death by projectile
- [ ] Death by falling
- [ ] All show "You Died!" message
- [ ] All allow restart with R key

## Performance Testing

### Test 11.1: Frame Rate
- [ ] Game runs smoothly (60 FPS)
- [ ] No noticeable lag during gameplay
- [ ] Moving platforms move smoothly
- [ ] Projectiles animate smoothly

### Test 11.2: Database Performance
- [ ] Database queries fast (<100ms)
- [ ] No delay showing leaderboard
- [ ] No delay saving record

## Database Verification Commands

### View all records:
```bash
sqlite3 jumpball.db "SELECT * FROM players ORDER BY completion_time_ms ASC;"
```

### Count total records:
```bash
sqlite3 jumpball.db "SELECT COUNT(*) FROM players;"
```

### View top 10:
```bash
sqlite3 jumpball.db "SELECT name, completion_time_ms FROM players ORDER BY completion_time_ms ASC LIMIT 10;"
```

### Clear all records (reset):
```bash
rm jumpball.db
```

## Expected Results Summary

### Complete Playthrough Timeline:
1. **00:00** - Launch game, see menu with empty leaderboard
2. **00:05** - Click "Start Game", enter name "TestPlayer"
3. **00:10** - Level 1 starts, timer begins
4. **01:00** - Complete Level 1 (average time ~50 seconds)
5. **01:02** - Level 2 (Boss) starts
6. **02:30** - Defeat boss (average time ~90 seconds)
7. **02:32** - Level 3 starts
8. **04:00** - Complete Level 3 (average time ~90 seconds)
9. **04:02** - Congrats dialog shows time "04:00.xxx"
10. **04:05** - Return to menu, leaderboard shows "TestPlayer"

### Database State After First Playthrough:
- 1 record in `players` table
- Name: "TestPlayer"
- Time: ~240000ms (4 minutes)
- Date: Current timestamp

### Leaderboard Display:
```
🏆 LEADERBOARD - TOP 10 🏆
┌─────────────────────────────────────┐
│ 1. TestPlayer - 04:00.123  (Gold)   │
└─────────────────────────────────────┘
```

## Common Issues & Solutions

### Issue: Game doesn't start
**Solution**: Check Java version (`java -version`), ensure JDK 11+

### Issue: No graphical display
**Solution**: Requires X11 or similar display server, won't work in pure terminal

### Issue: Database error
**Solution**: Check write permissions in directory, delete `jumpball.db` to reset

### Issue: Compilation fails
**Solution**: Ensure `sqlite-jdbc.jar` is in `/app` directory, check Java path

## Success Criteria

✅ All 3 levels playable  
✅ Database created and connected  
✅ Player name required and validated  
✅ Timer starts on "Start Game"  
✅ Timer runs across all levels  
✅ Leaderboard shown on menu (top 10)  
✅ Leaderboard shown in congrats (top 10)  
✅ Current player highlighted  
✅ Records persist between sessions  
✅ Level 3 has high difficulty  
✅ Level 3 has shooting enemies (5)  
✅ Level 3 has moving platforms (3)  
✅ Congrats popup appears automatically  
✅ All game mechanics work correctly  

## Test Report Template

```
Test Date: [Date]
Tester: [Name]
Java Version: [Version]
OS: [Operating System]

Phase 1: Menu & Database     [ ] Pass [ ] Fail
Phase 2: Player Name Entry   [ ] Pass [ ] Fail
Phase 3: Level 1             [ ] Pass [ ] Fail
Phase 4: Level 2 (Boss)      [ ] Pass [ ] Fail
Phase 5: Level 3 (Final)     [ ] Pass [ ] Fail
Phase 6: Timer System        [ ] Pass [ ] Fail
Phase 7: Congrats Dialog     [ ] Pass [ ] Fail
Phase 8: Database/Leaderboard[ ] Pass [ ] Fail
Phase 9: Visual Elements     [ ] Pass [ ] Fail
Phase 10: Edge Cases         [ ] Pass [ ] Fail

Overall Result: [ ] PASS [ ] FAIL

Notes:
___________________________________________
___________________________________________
___________________________________________
```

---

**Happy Testing!** 🎮🧪
