;;; Collector - a simple agent-scalable domain for graph movement dynamics
;;; Chris Argenta

(define (domain meetup)
  (:requirements :typing :strips)
  (:types place stuff agent)

  (:predicates
	(connected ?from ?to - place)
	(at ?agent - agent ?place - place)
	(location ?stuff - stuff ?place - place)
	(has ?agent - agent ?stuff - stuff)
	(OBS_move ?agent - agent ?from ?to - place)
  )

  (:action move
	:parameters (?agent - agent ?from ?to - place)
	:precondition (and  (connected ?from ?to)
			    (at ?agent ?from))
	:effect (and (at ?agent ?to)
		     (not (at ?agent ?from))
		     (OBS_Move ?agent ?from ?to))
  )

  (:action steal
	:parameters (?agent - agent ?place - place ?stuff - stuff)
	:precondition (and  (location ?stuff ?place)
			    (at ?agent ?place))
	:effect (and (not (location ?stuff ?place))
		     (has ?agent ?stuff)
		     (OBS_Steal ?agent ?place ?stuff))
  )

  (:action stash
	:parameters (?agent - agent ?place - place ?stuff - stuff)
	:precondition (and  (has ?agent ?stuff)
			    (at ?agent ?place))
	:effect (and (location ?stuff ?place)
		     (not (has ?agent ?stuff))
		     (OBS_Stash ?agent ?place ?stuff))
  )

)