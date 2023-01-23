from django import template
from re import sub
from django.utils.safestring import mark_safe

register = template.Library()

@register.filter(name="tohtml")
def tohtml(value):
    lines = sub("---", "<hr/>", value)
    heading1 = sub(r"\n# (.*)", r"<h2>\1</h2>", lines)
    heading2 = sub(r"\n## (.*)", r"<h3>\1</h3>", heading1)
    return mark_safe(heading2)
