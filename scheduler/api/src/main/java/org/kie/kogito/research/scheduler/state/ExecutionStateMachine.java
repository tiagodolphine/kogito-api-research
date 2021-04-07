package org.kie.kogito.research.scheduler.state;

interface ExecutionStateMachine {
    enum ExecutionState {         // fireAllRule | fireUntilHalt | executeTask <-- required action
        INACTIVE( false, true ),         // fire        | fire          | exec
        FIRING_UNTIL_HALT( true, true ), // do nothing  | do nothing    | enqueue
        INACTIVE_ON_FIRING_UNTIL_HALT( true, true ),
        HALTING( false, true ),          // wait + fire | wait + fire   | enqueue
        EXECUTING_TASK( false, true ),   // wait + fire | wait + fire   | wait + exec
        DEACTIVATED( false, true ),      // wait + fire | wait + fire   | wait + exec
        DISPOSING( false, false ),       // no further action is allowed
        DISPOSED( false, false );        // no further action is allowed

        private final boolean firing;
        private final boolean alive;

        ExecutionState( boolean firing, boolean alive ) {
            this.firing = firing;
            this.alive = alive;
        }

        public boolean isFiring() {
            return firing;
        }

        public boolean isAlive() {
            return alive;
        }
    }
}
