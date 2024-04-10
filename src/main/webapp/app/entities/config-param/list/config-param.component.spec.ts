import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ConfigParamService } from '../service/config-param.service';

import { ConfigParamComponent } from './config-param.component';

describe('ConfigParam Management Component', () => {
  let comp: ConfigParamComponent;
  let fixture: ComponentFixture<ConfigParamComponent>;
  let service: ConfigParamService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'config-param', component: ConfigParamComponent }]),
        HttpClientTestingModule,
        ConfigParamComponent,
      ],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              }),
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(ConfigParamComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ConfigParamComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ConfigParamService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        }),
      ),
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.configParams?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to configParamService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getConfigParamIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getConfigParamIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
