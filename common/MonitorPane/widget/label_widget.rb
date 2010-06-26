#
# An abstraction of a widget containing stylized text
#
# June 2010
# Copyright Zanoccio LLC.  All rights reserved.
#

class LabelWidget < AbstractWidget
  attr_accessor :style
  attr_accessor :text
  attr_accessor :padding

  def initialize(text, style=nil)
    @text = text
    @style = style

    @padding = 1
  end

  def get_width
    @text.length + (2 * @padding)
  end
end
