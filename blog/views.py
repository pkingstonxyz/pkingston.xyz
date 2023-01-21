from django.views.generic.list import ListView
from blog.models import Post

class PostListView(ListView):
    model = Post
    paginate_by = 20

