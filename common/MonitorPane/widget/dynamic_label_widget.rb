require 'widget/label_widget'

class DynamicLabelWidget < LabelWidget
  def initialize(&textfn)
    super('');
    @textfn = textfn
  end

  def render(y,x)
    @text = @textfn.call
    super(y,x)
  end
end
