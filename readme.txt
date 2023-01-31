	
Aim Trainer Program Features:

Part 1 
	- The user can create a new target by Shift-clicking on the background 
	- The user can move a target by clicking on the target and dragging 
	- The user can resize a target by Shift-clicking on a target and then dragging left or right (only dX is used to change the size) 
	- The user can select a target (shown by drawing the target in a different colour) by clicking on the target 
	- The user can clear the selection by clicking on the background 
	- The user can delete a selected target by pressing the Delete key on the keyboard 
	- Targets show their order number (i.e., the order they are stored in the model) at the centre of the circle 

Part 2
	- All create, delete, move, and resize actions can be undone/redone 
	- When the user presses Control-Z, the previous action (if there is one) is undone 
	- When the user presses Control-R, the next action (if there is one) is redone 
	- Undo/Redo actions accommodate any multiple selection that was part of the original action (e.g., moving multiple targets) 

Part 3
	- If there is a selection, pressing Control-X puts the selected items on the app’s clipboard and removes them from the model 
		> The action of removing the items from the model is undoable 
	- If there is a selection, pressing Control-C puts a copy of the selected items on the app’s clipboard (and does not remove them from the model) 
		> Only one collection of items can be in the clipboard at a time; if the user copies a second selection, the first one is discarded from the clipboard 
	- Pressing Control-V puts a copy of the items that are in the clipboard into the model 
		> The action of adding the items to the model is undoable 
		> The user can paste the items in the clipboard multiple times 
		
Part 4	
	- When the user presses Control-T, the system switches to a “target trainer” view 
		> The view shows one target at a time, in the order that they are stored 
		> When the user clicks on the target, the next target is shown 
		> When all targets have been selected, the system shows a report view 
		> The system records the elapsed time between the start and end of each trial 
		> The system also records the index of difficulty (ID), based on the distance between the previous target and the current target
	- The report view shows a chart of the user’s performance 
		> The chart plots MT (movement time in milliseconds) against ID (index of difficulty) with a dot for each trial 
	- If the user presses Control-E, the system immediately switches back to the editor view
	- When the report view is active, the user can press Control-T to restart the test, or Control-E to go back to the editor 
