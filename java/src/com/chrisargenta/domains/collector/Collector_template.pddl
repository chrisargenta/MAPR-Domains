(define (problem ###PROBLEM###) (:domain collector) (:requirements :strips :typing)
	(:objects
		Barcelona - place
		Paris - place
		Brussels - place
		Milan - place
		Hamburg - place
		Munich - place
		Berlin - place
		Copenhagen - place
		Vienna - place
		Warsaw - place
		Belgrade - place
		Bucharest - place
		Minsk - place
		Kiev - place

		###OBJECTSAGENTS###

		###OBJECTSOTHER###
		
	)

	(:init

		(connected Barcelona Paris)
		(connected Barcelona Milan)
		(connected Paris Brussels)
		(connected Paris Munich)
		(connected Paris Milan)
		(connected Paris Barcelona)
		(connected Brussels Paris)
		(connected Brussels Munich)
		(connected Brussels Hamburg)
		(connected Milan Paris)
		(connected Milan Munich)
		(connected Milan Belgrade)
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
		(connected Belgrade Milan)
		(connected Bucharest Warsaw)
		(connected Bucharest Kiev)
		(connected Bucharest Belgrade)
		(connected Minsk Kiev)
		(connected Minsk Warsaw)
		(connected Kiev Bucharest)
		(connected Kiev Minsk)
		
		###INITAGENTS###

		###INITOTHER###
		
	)
	(:goal (and
		###GOAL###
	))
)