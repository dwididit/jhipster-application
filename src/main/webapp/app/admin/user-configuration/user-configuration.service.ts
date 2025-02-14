import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from 'app/core/config/application-config.service';

export interface TemplateProperty {
  index: number;
  label: string;
  name: string;
  show: boolean;
  strict: boolean;
}

@Injectable({ providedIn: 'root' })
export class UserConfigurationService {
  private resourceUrl = this.applicationConfigService.getEndpointFor('api/user-configurations');

  constructor(
    private http: HttpClient,
    private applicationConfigService: ApplicationConfigService,
  ) {}

  updateTemplateProperties(userId: string, templateName: string, properties: TemplateProperty[]): Observable<any> {
    return this.http.put(`${this.resourceUrl}/${userId}/templates/${templateName}/properties`, properties);
  }

  getTemplateProperties(userId: string, templateName: string): Observable<TemplateProperty[]> {
    return this.http.get<TemplateProperty[]>(`${this.resourceUrl}/${userId}/templates/${templateName}/properties`);
  }
}
