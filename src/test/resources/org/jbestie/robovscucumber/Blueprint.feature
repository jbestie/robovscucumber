Feature: Blueprint
  Scenario: Send ActiveMQ message
    Given message Hello there to send
    When user sends the message to queue testqueue
    Then message in queue valid is Hello there