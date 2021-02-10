Feature: Training on Lingo

  As a User,
  I want to practice Lingo by guessing 5, 6 & 7 letter words,
  In order to prepare for Lingo.

  Scenario: Start new game
    When I start a new game
    Then I have to guess a word with "5" letters
    And I should see the first letter

  Scenario Outline: Start a new round
    Given I am playing a game
    And the round was won
    And the last word had "<previous length>" letters
    When I start a new round
    Then the word to guess has "<next length>" letters
    Examples:
      | previous length | next length |
      | 5               | 6           |
      | 6               | 7           |
      | 7               | 5           |

    # Failure path
    Given I am playing a game
    And the round was lost
    Then I cannot start a new round


  Scenario Outline: Guess a correct word
    Given I am playing a game
    When I fillout "<guess>" for to be guessed "<word>"
    Then I will receive "<feedback>"as feedback
    Examples:
      | guess | word | feedback |
      | plank | lingo | ABSENT, PRESENT, ABSENT, PRESENT, ABSENT |
      | breee | lingo | ABSENT, ABSENT, ABSENT, ABSENT, ABSENT |
      | iiiii | lingo | PRESENT, CORRECT, PRESENT, PRESENT, PRESENT |
      | lingo | lingo | CORRECT, CORRECT, CORRECT, CORRECT, CORRECT |

    #failure path
    Given I am playing a game
    When My guess does not have the same length as the to be guessed word
    Then I will receive feedback that the length of my guess is incorrect

  # 1.    Na een succesvolle raadpoging wordt de score verhoogd
  Scenario: Win a game
    Given I am playing a game
    When I guess the to be guessed word
    Then I should receive score points

  # 2.    Na vijf raadpogingen binnen een ronde is de speler af
  #failure path
    Given I am playing a game
    When I guessed 5 times incorrectly
    Then I lose the game

  # 3.    Speler kan geen woord raden als het woord al geraden is
  #failure path
    Given I am playing a game
    When I guessed that I guessed earlier
    Then I will receive feedback that I already guessed that word

  # 4.    Speler kan geen woord raden als speler af is
  #failure path
    Given That i lost a game
    When when i try to guess
    Then I will receive feedback that I can not proceed a game that I lost

  # 5.    Speler kan geen nieuwe ronde starten als speler nog aan het raden is
  #failure path
    Given I am playing a game / I started a game
    When I start a game
    Then I will receive feedback that I have to finnish my current game

  # 6.    Speler kan geen nieuwe ronde starten als speler geen spel heeft
  #failure path
    Given I have not started a game
    When I start an other round
    Then I will receive feedback that I can not start a round because I did not start a game