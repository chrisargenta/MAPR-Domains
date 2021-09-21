;;;
;;; TEAMBLOCKS - a multiagent blocks world
;;;

(define (domain TEAMBLOCKS)
  (:requirements :strips :typing :equality)
  (:types block agent)
  (:predicates (on ?x ?y - block)
	       (ontable ?x - block)
	       (clear ?x - block)
	       (handempty ?x - agent)
	       (holding ?agent - agent ?y - block)
           (isFree ?x - block)
           (OBS_pick-up ?agent - agent ?x - block)
           (OBS_put-down ?agent - agent ?y - block)
           (OBS_stack ?agent - agent ?x ?y - block)
           (OBS_unstack ?agent - agent ?x ?y - block)
  )

  (:action pick-up	     
         :parameters (?agent - agent ?block - block )
	     :precondition (and (clear ?block) (ontable ?block) (handempty ?agent) (isFree ?block))
	     :effect
	     (and (not (ontable ?block))
		   (not (clear ?block))
		   (not (handempty ?agent))
		   (holding ?agent ?block)
           (OBS_pick-up ?agent ?block)
           (not (isFree ?block))
         )
  )

  (:action put-down
	     :parameters (?agent - agent ?y - block)
	     :precondition (holding ?agent ?y)
	     :effect
	     (and (not (holding ?agent ?y))
		   (clear ?y)
		   (handempty ?agent)
		   (ontable ?y)
                   (OBS_put-down ?agent ?y)
		   (isFree ?y)))
  (:action stack
	     :parameters (?agent - agent ?x ?y - block)
	     :precondition (and (holding ?agent ?x) (clear ?y) (isFree ?y) (not (= ?x ?y)))
	     :effect
	     (and (not (holding ?agent ?x))
		   (not (clear ?y))
		   (clear ?x)
		   (handempty ?agent)
                   (isFree ?x)
                   (OBS_stack ?agent ?x ?y)
		   (on ?x ?y)))

  (:action unstack
	     :parameters (?agent - agent ?x ?y - block)
	     :precondition (and (on ?x ?y) (clear ?x) (handempty ?agent) (isFree ?x) (isFree ?y) (not (= ?x ?y)))
	     :effect
	     (and (holding ?agent ?x)
		   (clear ?y)
		   (not (clear ?x))
		   (not (handempty ?agent))
                   (not (isFree ?x))
                   (OBS_unstack ?agent ?x ?y)
		   (not (on ?x ?y)))))
