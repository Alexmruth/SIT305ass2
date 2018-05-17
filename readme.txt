Alex Ruth SID: 216106075

APP TITLE: Cave Crawler
PLATFORM: Android
GITHUB: https://github.com/Alexmruth/SIT305ass2

OVERVIEW: You play as an adventurer who has crashed his plane on a mysterious mountain. You must escape the mountain 
through a series of caves. Defeat hoards of enemies and buy upgrades along the way to help you reach the end!

MAJOR FEATURES:
Battle enemies:
- Battle random enemies and kill them before they kill you! Use health potions to stay alive.
Buy upgrades/weapons:
-Buy weapons and equip them to increase your attack output or increase you maximum health.
Random encounters:
- Come across an NPC on each level and have the ability to have conversation with them.
Save player data:
- Save player progress as you g0o along so you don’t have to start all over, or start new game and wipe current progress.
———————————————————————————————————————————————————————————————————————
Compile instructions:
- Software: Android Studio
- Emulation: Run app (Shift +F10)

Directory structure:
- Images:           app\src\main\res\drawable
- JSON file/s:      app\src\main\assets
- Java files:       app\src\main\java\com\example\alexuni\sit305ass2
- XML layout files: app\src\main\res\layout

———————————————————————————————————————————————————————————————————————

# Henry comments 13/April
- Your JSON test file isn't in the correct syntax :) . Use a JSON validator online to check it.
- Not enough commits + changelog items to pass at this frequency.
- You're still missing a licenses.txt file.


# Henry comments 17/April
- Great fixes. May I make one suggestion. When doing conversations, if you want them to be in order, use an array, not a map with keys.
Not this:
  "introText1": "Hello there, are you okay? You seemed to have crashed your plane.",
  "introText2": "... Ugh, where am I?",
  
But this:
  "Hello there, are you okay? You seemed to have crashed your plane.",
  "... Ugh, where am I?",
  
Or even this:

  {"id": "introText1", "character": "xx", "text": "Hello there, are you okay? You seemed to have crashed your plane."},
  {"id": "introText2", "character": "xx", "text": "... Ugh, where am I?"},
  
... that might make it easier for you to then go through each conversation and maybe have options in each speech line to go to other lines. E.g.
  {"id": "introText2", "character": "xx", "text": "... Ugh, where am I?", "answers": [{"I want out of here": "introText2", "I want to buy ice cream": "shopLine1"]},
  
Maybe something like that. There are many ways to do it, it just depends on what information would be most helpful to you for each line of speech, and the speech overall.


## Thanks Henry! I appreciate the suggestions! Implementing it has significantly reduced  the amount of code I use.

# Henry comments 27/April
- Great to see progress.
- Your changelog needs a lot of work :) It should match commits at least.
- Your data hasn't quite progressed enough given we're at the end of Week 7.




