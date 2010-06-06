# To change this template, choose Tools | Templates
# and open the template in the editor.

class StatusItem
  attr_accessor :message

  def initialize(message)
    @message = message
  end

  def draw()
    '00000123  losangeles1  HTTP                    [ QUEUE ]'+
    'http://google.com/'
  end
end
