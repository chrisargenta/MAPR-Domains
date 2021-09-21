(define (problem ###PROBLEM###)
  (:domain storybook)
  (:requirements :strips :typing :negative-preconditions) 
  (:objects
	horse bear - vehicle
	sword club bow - weapon
	cave town stable blacksmith castle field tower forest garden - place

	###OBJECTSOTHER###		

	###OBJECTSAGENTS###

  )
  (:init
		(haspath town town)
		(haspath town blacksmith)
		(haspath blacksmith town)
		(haspath town stable)
		(haspath stable town)
		(haspath town field)
		(haspath field town)
		(haspath forest field)
		(haspath field forest)
		(haspath town garden)
		(haspath garden town)
		(haspath garden tower)
		(haspath tower garden)
		(haspath town castle)
		(haspath castle town)
		(haspath cave field)
		(haspath field cave)
		(haspath forest tower)
		(haspath tower forest)
		(haspath castle blacksmith)
		(haspath blacksmith castle)
		(haspath stable garden)
		(haspath garden stable)

        (placed club forest)
        (sells sword blacksmith)
        (placed bow garden)

        (parked horse stable)
        (parked bear forest)

        (vulnerable witch club)
        (vulnerable witch sword)
        (vulnerable dragon sword)
        (vulnerable giant bow)
        (vulnerable giant sword)

        (isroyal princess)
        
	###INITOTHER###
	###INITAGENTS###
  )
  (:goal (and
	###GOAL###
  ))
)
