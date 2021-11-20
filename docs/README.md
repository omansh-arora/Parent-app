A general design in iteration II
================================

Overall we are providing a coin flip service on multiple tasks for multiple kids.

Ideally there should be one default task and one default anonymous child. This default child
can flip a coin on the default task again and again with or without the history being kept.

Then we add support to multiple kids, such that they can take turns to flip the next coin in a fair
game. Also history is recorded to keep track on the success / failure trials.

Then we add support to multiple tasks, for each task there is a unique queue to keep track on who is
the next kid to play.

The reason we are not using a global queue for all tasks is that one of the requirements says that
different task would like to have its own choice of next kid to flip its coin.

Further more, we need to support manual selection of the next candidate to override the original
calculated one in the queue.

For data such as tasks, children, queues, and history, we would like to keep them in the local storage so that
data is kept among each run of the application.

Local Storage
-------------

Let us think about what change most during the usage of this application. We might add/edit/delete
a child or a task, although these operations are assumed to be rare, compared with operations like:
current queue for current task, history after each flip, which updates way more frequently.

add / delete / edit a child: -> update every queue for every task accordingly.

add a task: -> create a default queue for it.
delete a task: -> also delete its history, and its queue.
edit a task: -> just update the description should be fine.

queue: -> there is no direct operation on the queue except pushing the current child
            into the end of the queue after his/her flipping the last coin.

history: -> view only.

Classes
-------

### ChildManager
### Child
### TaskManager
### Task
### CoinFlip
### CoinFlipQueue
### CoinFlipHistory

UI
--

The main functionality locates at the CoinFlipActivity where a task is associated. Either the default
anonymous child or the child at the front of the queue can flip a coin to generate a result which kept in task history.

