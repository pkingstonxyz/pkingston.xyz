from django.views.generic.list import ListView
from django.views.generic.detail import DetailView
from blog.models import Post

class PostListView(ListView):
    model = Post
    paginate_by = 5

class PostDetailView(DetailView):
    model = Post
