# To change this template, choose Tools | Templates
# and open the template in the editor.

require 'ncurses'

class MonitorPane

  attr_accessor :title
  attr_accessor :width
  attr_accessor :auto_resize
  attr_accessor :scroll

  def initialize(title)
    @auto_resize = false
    @scroll = true

    @title = title
  end

  def render
    
  end
end
