import { NgModule } from '@angular/core';
import { LucideAngularModule } from 'lucide-angular';
import { Home, FileText, PlusSquare, Users, Settings } from 'lucide-angular';

const icons = {
  Home,
  FileText,
  PlusSquare,
  Users,
  Settings,
};

@NgModule({
  imports: [LucideAngularModule.pick(icons)],
  exports: [LucideAngularModule],
})
export class LucideIconsModule {}
