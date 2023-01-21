from django.views.generic.list import ListView
from django.views.generic import TemplateView
from .models import Note

from django.shortcuts import render
from django.shortcuts import redirect

class NoteListView(ListView):
    model = Note
    template_name = 'notes/note_list.html'
    context_object_name = 'note_list'

    def get_queryset(self):
        return self.model.objects.order_by('-date')

class AddView(TemplateView):
    template_name = 'notes/note_add.html'

    def get(self, request, *args, **kwargs):
        return render(request, self.template_name)

    def post(self, request, *args, **kwargs):
        if request.POST['secret'] != "beans":
            #redirect and do nothing
            print(request.POST['secret'])
            return redirect('notes')

        note = Note()
        note.title = request.POST['title']
        note.body = request.POST['body']
        note.save()
        return redirect('notes')

