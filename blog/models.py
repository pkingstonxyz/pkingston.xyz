from django.db import models

class Post(models.Model):
    title = models.CharField(max_length=200, unique=True)
    tagline = models.TextField(default="")
    slug = models.SlugField(max_length=200, unique=True)
    created_on = models.DateTimeField(auto_now_add=True)
    content = models.TextField()

    class Meta:
        ordering = ['-created_on']

    def __str__(self):
        return self.title
