# To change this template, choose Tools | Templates
# and open the template in the editor.

class WidgetContainer
  def initialize(ypos, xpos, widget)
    @xpos = xpos
    @ypos = ypos
    @widget = widget
  end

  def render
    @widget.render(@ypos, @xpos)
  end
end
