(define (domain storybook)
  (:requirements :strips :typing :negative-preconditions :equality) 
  (:types 
	weapon place monster folk agent treasure vehicle
  )

  (:predicates
  
;; MONSTERS guard treasure, get killed, make for good tales
 	(at ?monster - monster ?place - place)
 	(guarding ?monster - monster ?treasure - treasure)
 	(vulnerable ?monster - monster ?weapon - weapon)
	(conquest ?agent - agent ?monster - monster)
	(dead ?monster - monster)
	(notorious ?monster - monster)
	
;;; FOLK are imprisoned, fall in love, and get married
	(at ?folk - folk ?place - place)
	(imprisoned ?folk - folk ?monster - monster)
	(imprisoned ?folk - folk ?place - place)
	(available ?folk - folk)
	(isroyal ?folk - folk)
	(loves ?folk - folk ?agent - agent )
	
;; WEAPONS lie around, carried about, are sold, used in battles
	(wields ?agent - agent ?weapon - weapon)
	(sells ?weapon - weapon ?place - place)
	(placed ?weapon - weapon ?place - place)

;; TREASURES
	(have ?agent - agent ?treasure - treasure)
	(placed_t ?treasure - treasure ?place - place)

;; AGENT STATUS weddings, abilities, and status
	(at ?agent - agent ?place - place)
	(likedbyking ?agent - agent)
	(canride ?agent - agent)
	(single ?agent - agent)
	(emptyhanded ?agent - agent)
	(rested ?agent - agent)
	
;; ENVIRONMENT
	(haspath ?from - place ?to - place)
	(parked ?vehicle - vehicle ?place - place)
	
;; Quests - these are the goals that agents should seek
	(findlove)
	(havehonor)
	(befamous)
	(getrich)

;;; Observations - these are used in recognition
	(OBS_WALK ?agent - agent ?from - place ?to - place)
	(OBS_RIDE ?agent - agent ?vehicle - vehicle ?from - place ?to - place)
	(OBS_REST ?agent - agent ?place - place)
	(OBS_BUY_WEAPON ?agent - agent ?weapon - weapon ?place - place)
	(OBS_PICKUP_WEAPON ?agent - agent ?weapon - weapon ?place - place)
	(OBS_DROP_WEAPON ?agent - agent ?weapon - weapon ?place - place)
	(OBS_PICKUP_TREASURE ?agent - agent ?treasure - treasure ?place - place)
	(OBS_DROP_TREASURE ?agent - agent ?treasure - treasure ?place - place)
	(OBS_AMASS_WEALTH ?agent - agent ?treasure1 - treasure ?treasure2 - treasure ?treasure3 - treasure)
	(OBS_SLAY ?agent - agent ?monster - monster ?weapon - weapon ?place - place)
	(OBS_RESCUE ?agent - agent ?folk - folk ?monster - monster ?place - place)
	(OBS_PLUNDER ?agent - agent ?treasure - treasure ?monster - monster ?place - place)
	(OBS_MARRY_FOR_MONEY ?agent - agent ?folk - folk)
	(OBS_MARRY_FOR_LOVE ?agent - agent ?folk - folk)
	(OBS_TELL_TRUTH ?agent - agent ?monster - monster ?about - agent)
	(OBS_TELL_TALE ?agent - agent ?monster - monster ?about - agent)
  )

;;; Mobility Actions - move agents around environment, walking makes agents tired so they have to rest before going on

  (:action WALK
	:parameters (?agent - agent ?from - place ?to - place)
	:precondition (and (at ?agent ?from) (haspath ?from ?to))
	:effect (and (not (at ?agent ?from)) (at ?agent ?to) (not (rested ?agent)) (OBS_WALK ?agent ?from ?to))
  )

  (:action RIDE
	:parameters (?agent - agent ?vehicle - vehicle ?from - place ?to - place)
	:precondition (and (at ?agent ?from) (haspath ?from ?to) (parked ?vehicle ?from) (canride ?agent))
	:effect (and (not (at ?agent ?from)) (at ?agent ?to) (not (parked ?vehicle ?from)) (parked ?vehicle ?to) (rested ?agent) (OBS_RIDE ?agent ?vehicle ?from ?to))
  )

  (:action REST
	:parameters (?agent - agent ?place - place)
	:precondition (at ?agent ?place)
	:effect (and (rested ?agent) (OBS_REST ?agent ?place))
  )


;;; Weapon Handling

  (:action BUY_WEAPON
	:parameters (?agent - agent ?weapon - weapon ?place - place)
	:precondition (and (at ?agent ?place) (sells ?weapon ?place) (emptyhanded ?agent))
	:effect (and (wields ?agent ?weapon) (not (emptyhanded ?agent)) (OBS_BUY_WEAPON ?agent ?weapon ?place))
  )

  (:action PICKUP_WEAPON
	:parameters (?agent - agent ?weapon - weapon ?place - place)
	:precondition (and (at ?agent ?place) (placed ?weapon ?place) (emptyhanded ?agent))
	:effect (and (wields ?agent ?weapon) (not (placed ?weapon ?place)) (not (emptyhanded ?agent)) (OBS_PICKUP_WEAPON ?agent ?weapon ?place))
  )

  (:action DROP_WEAPON
	:parameters (?agent - agent ?weapon - weapon ?place - place)
	:precondition (and (at ?agent ?place) (wields ?agent ?weapon) (not (emptyhanded ?agent)))
	:effect (and (not (wields ?agent ?weapon)) (placed ?weapon ?place) (emptyhanded ?agent) (OBS_DROP_WEAPON ?agent ?weapon ?place))
  )

;;; Treasure Handling

  (:action PICKUP_TREASURE
	:parameters (?agent - agent ?treasure - treasure ?place - place )
	:precondition (and (at ?agent ?place) (placed_t ?treasure ?place))
	:effect (and (have ?agent ?treasure) (not (placed_t ?treasure ?place)) (OBS_PICKUP_TREASURE ?agent ?treasure ?place))
  )

  (:action DROP_TREASURE
	:parameters (?agent - agent ?treasure - treasure ?place - place )
	:precondition (and (at ?agent ?place) (have ?agent ?treasure))
	:effect (and (not (have ?agent ?treasure)) (placed_t ?treasure ?place) (OBS_DROP_TREASURE ?agent ?treasure ?place))
  )


;;; Heroic Actions

  (:action SLAY
	:parameters (?agent - agent ?monster - monster ?weapon - weapon ?place - place)
	:precondition (and (at ?agent ?place) (at ?monster ?place) (not (dead ?monster)) (wields ?agent ?weapon) (vulnerable ?monster ?weapon) (rested ?agent))
	:effect (and (dead ?monster) (conquest ?agent ?monster) (not (wields ?agent ?weapon)) (emptyhanded ?agent) (OBS_SLAY ?agent ?monster ?weapon ?place))
  )

  (:action RESCUE
	:parameters (?agent - agent ?folk - folk ?monster - monster ?place - place)
	:precondition (and (at ?agent ?place) (at ?monster ?place) 
		(dead ?monster) (imprisoned ?folk ?monster))
	:effect (and (not (imprisoned ?folk ?monster)) (available ?folk) (loves ?folk ?agent) (OBS_RESCUE ?agent ?folk ?monster ?place))
  )

  (:action PLUNDER 
	:parameters (?agent - agent ?treasure - treasure ?monster - monster ?place - place)
	:precondition (and (at ?agent ?place) (at ?monster ?place) (dead ?monster) (guarding ?monster ?treasure))
	:effect (and (not (guarding ?monster ?treasure)) (have ?agent ?treasure) (OBS_PLUNDER ?agent ?treasure ?monster ?place))
  )

  (:action MARRY_FOR_MONEY
	:parameters (?agent - agent ?folk - folk)
	:precondition (and (isroyal ?folk) (available ?folk) (likedbyking ?agent) (single ?agent))
	:effect (and (getrich) (not (available ?folk)) (not (single ?agent)) (not (havehonor)) (OBS_MARRY_FOR_MONEY ?agent ?folk))
  )

  (:action MARRY_FOR_LOVE
	:parameters (?agent - agent ?folk - folk)
	:precondition (and (available ?folk) (loves ?folk ?agent) (single ?agent))
	:effect (and (findlove) (not (available ?folk)) (not (single ?agent)) (OBS_MARRY_FOR_LOVE ?agent ?folk))
  )

  (:action TELL_TRUTH
	:parameters (?agent - agent ?monster - monster ?about - agent)
	:precondition (and (dead ?monster) (notorious ?monster) (conquest ?about ?monster))
	:effect (and (befamous) (likedbyking ?about) (havehonor) (OBS_TELL_TRUTH ?agent ?monster ?about))
  )

  (:action TELL_TALE
	:parameters (?agent - agent ?monster - monster ?about - agent)
	:precondition (and (dead ?monster) (notorious ?monster) (not (conquest ?about ?monster)))
	:effect (and (befamous) (likedbyking ?about) (not (havehonor)) (OBS_TELL_TALE ?agent ?monster ?about))
  )
  
  (:action AMASS_WEALTH
 	:parameters (?agent - agent ?treasure1 - treasure ?treasure2 - treasure)
 	:precondition (and (have ?agent ?treasure1) (have ?agent ?treasure2) (not (= ?treasure1 ?treasure2)) )
 	:effect (and (getrich) (OBS_AMASS_WEALTH ?agent ?treasure1 ?treasure2))
  )
)
