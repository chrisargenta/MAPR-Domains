(define (problem ###PROBLEM###) (:domain collector) (:requirements :strips :typing)
	(:objects
		Madrid - place
		Barcelona - place
		Paris - place
		London - place
		Brussels - place
		Milan - place
		Hamburg - place
		Munich - place
		Berlin - place
		Copenhagen - place
		Stockholm - place
		Rome - place
		Vienna - place
		Warsaw - place
		Belgrade - place
		Bucharest - place
		Minsk - place
		Kiev - place
		StPetersburg - place
		Moscow - place

		###OBJECTSAGENTS###

		###OBJECTSOTHER###
		
	)

	(:init
		(connected Madrid Barcelona)
		(connected Madrid Paris)
		(connected Barcelona Madrid)
		(connected Barcelona Paris)
		(connected Barcelona Milan)
		(connected Paris London)
		(connected Paris Brussels)
		(connected Paris Munich)
		(connected Paris Milan)
		(connected Paris Barcelona)
		(connected Paris Madrid)
		(connected London Paris)
		(connected London Brussels)
		(connected Brussels London)
		(connected Brussels Paris)
		(connected Brussels Munich)
		(connected Brussels Hamburg)
		(connected Milan Paris)
		(connected Milan Munich)
		(connected Milan Belgrade)
		(connected Milan Rome)
		(connected Milan Barcelona)
		(connected Hamburg Brussels)
		(connected Hamburg Copenhagen)
		(connected Hamburg Berlin)
		(connected Munich Paris)
		(connected Munich Brussels)
		(connected Munich Berlin)
		(connected Munich Vienna)
		(connected Munich Milan)
		(connected Berlin Hamburg)
		(connected Berlin Copenhagen)
		(connected Berlin Warsaw)
		(connected Berlin Munich)
		(connected Copenhagen Hamburg)
		(connected Copenhagen Berlin)
		(connected Copenhagen Stockholm)
		(connected Stockholm Copenhagen)
		(connected Stockholm StPetersburg)
		(connected Rome Milan)
		(connected Rome Belgrade)
		(connected Vienna Munich)
		(connected Vienna Warsaw)
		(connected Vienna Bucharest)
		(connected Vienna Belgrade)
		(connected Warsaw Berlin)
		(connected Warsaw Minsk)
		(connected Warsaw Bucharest)
		(connected Warsaw Vienna)
		(connected Belgrade Vienna)
		(connected Belgrade Bucharest)
		(connected Belgrade Rome)
		(connected Belgrade Milan)
		(connected Bucharest Warsaw)
		(connected Bucharest Kiev)
		(connected Bucharest Belgrade)
		(connected Minsk Moscow)
		(connected Minsk Kiev)
		(connected Minsk Warsaw)
		(connected Kiev Moscow)
		(connected Kiev Bucharest)
		(connected Kiev Minsk)
		(connected StPetersburg Stockholm)
		(connected StPetersburg Moscow)
		(connected Moscow StPetersburg)
		(connected Moscow Minsk)
		(connected Moscow Kiev)
		
		###INITAGENTS###

		###INITOTHER###
		
	)
	(:goal (and
		###GOAL###
	))
)