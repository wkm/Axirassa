# To change this template, choose Tools | Templates
# and open the template in the editor.

class RowWidget < AbstractWidget
  attr_reader :widgets

  def add_widget(widget)
    @widgets << widget
  end
end
