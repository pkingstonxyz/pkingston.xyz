from django.db import models
import random

class Note(models.Model):
    title = models.CharField(max_length=50)
    body = models.TextField(blank=True)
    date = models.DateTimeField(auto_now=True)
    color = models.CharField(max_length=7)
    secondary_color = models.CharField(max_length=7)

    def save(self, *args, **kwargs):
        colors = {"#052f5f": "#084D9B",
                  "#E03616": "#A72A11",
                  "#8CB369": "#6E954B",
                  "#B287A3": "#A06A8D",
                  "#550C18": "#8F2429"}
        if not self.pk:
            self.color = random.choice(list(colors.keys()))
            self.secondary_color = colors[self.color]
        super().save(*args, **kwargs)
    def __str__(self):
        return self.title
