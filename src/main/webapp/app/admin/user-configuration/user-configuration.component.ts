import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbTooltipModule } from '@ng-bootstrap/ng-bootstrap';

interface UserField {
  index: number;
  label: string;
  name: string;
  show: boolean;
  strict: boolean;
}

@Component({
  selector: 'jhi-user-configuration',
  templateUrl: './user-configuration.component.html',
  standalone: true,
  imports: [FormsModule, NgbTooltipModule],
})
export class UserConfigurationComponent {
  fields = signal<UserField[]>([
    { index: 1, label: 'ID', name: 'id', show: true, strict: true },
    { index: 2, label: 'Login', name: 'login', show: true, strict: true },
    { index: 3, label: 'Email', name: 'email', show: true, strict: true },
    { index: 4, label: 'Activated', name: 'activated', show: true, strict: true },
    { index: 5, label: 'Language', name: 'langKey', show: true, strict: true },
    { index: 6, label: 'Profiles', name: 'authorities', show: true, strict: true },
    { index: 7, label: 'Created date', name: 'createdDate', show: true, strict: true },
    { index: 8, label: 'Modified by', name: 'lastModifiedBy', show: true, strict: true },
    { index: 9, label: 'Modified date', name: 'lastModifiedDate', show: true, strict: true },
  ]);

  toggleField(field: UserField): void {
    if (!field.strict) {
      field.show = !field.show;
      this.fields.update(currentFields => [...currentFields]);
    }
  }
}
