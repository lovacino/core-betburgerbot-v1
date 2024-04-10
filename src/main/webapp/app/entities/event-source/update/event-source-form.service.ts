import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IEventSource, NewEventSource } from '../event-source.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEventSource for edit and NewEventSourceFormGroupInput for create.
 */
type EventSourceFormGroupInput = IEventSource | PartialWithRequiredKeyOf<NewEventSource>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IEventSource | NewEventSource> = Omit<T, 'koefLastModifiedAt' | 'scannedAt' | 'startedAt' | 'updatedAt'> & {
  koefLastModifiedAt?: string | null;
  scannedAt?: string | null;
  startedAt?: string | null;
  updatedAt?: string | null;
};

type EventSourceFormRawValue = FormValueOf<IEventSource>;

type NewEventSourceFormRawValue = FormValueOf<NewEventSource>;

type EventSourceFormDefaults = Pick<NewEventSource, 'id' | 'koefLastModifiedAt' | 'scannedAt' | 'startedAt' | 'updatedAt'>;

type EventSourceFormGroupContent = {
  id: FormControl<EventSourceFormRawValue['id'] | NewEventSource['id']>;
  home: FormControl<EventSourceFormRawValue['home']>;
  away: FormControl<EventSourceFormRawValue['away']>;
  league: FormControl<EventSourceFormRawValue['league']>;
  eventName: FormControl<EventSourceFormRawValue['eventName']>;
  bookmakerEventId: FormControl<EventSourceFormRawValue['bookmakerEventId']>;
  betTypeParam: FormControl<EventSourceFormRawValue['betTypeParam']>;
  koefLastModifiedAt: FormControl<EventSourceFormRawValue['koefLastModifiedAt']>;
  scannedAt: FormControl<EventSourceFormRawValue['scannedAt']>;
  startedAt: FormControl<EventSourceFormRawValue['startedAt']>;
  updatedAt: FormControl<EventSourceFormRawValue['updatedAt']>;
  betBurgerId: FormControl<EventSourceFormRawValue['betBurgerId']>;
  koef: FormControl<EventSourceFormRawValue['koef']>;
  bookmaker: FormControl<EventSourceFormRawValue['bookmaker']>;
  sport: FormControl<EventSourceFormRawValue['sport']>;
  period: FormControl<EventSourceFormRawValue['period']>;
  betType: FormControl<EventSourceFormRawValue['betType']>;
};

export type EventSourceFormGroup = FormGroup<EventSourceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EventSourceFormService {
  createEventSourceFormGroup(eventSource: EventSourceFormGroupInput = { id: null }): EventSourceFormGroup {
    const eventSourceRawValue = this.convertEventSourceToEventSourceRawValue({
      ...this.getFormDefaults(),
      ...eventSource,
    });
    return new FormGroup<EventSourceFormGroupContent>({
      id: new FormControl(
        { value: eventSourceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      home: new FormControl(eventSourceRawValue.home),
      away: new FormControl(eventSourceRawValue.away),
      league: new FormControl(eventSourceRawValue.league),
      eventName: new FormControl(eventSourceRawValue.eventName),
      bookmakerEventId: new FormControl(eventSourceRawValue.bookmakerEventId),
      betTypeParam: new FormControl(eventSourceRawValue.betTypeParam),
      koefLastModifiedAt: new FormControl(eventSourceRawValue.koefLastModifiedAt),
      scannedAt: new FormControl(eventSourceRawValue.scannedAt),
      startedAt: new FormControl(eventSourceRawValue.startedAt),
      updatedAt: new FormControl(eventSourceRawValue.updatedAt),
      betBurgerId: new FormControl(eventSourceRawValue.betBurgerId),
      koef: new FormControl(eventSourceRawValue.koef),
      bookmaker: new FormControl(eventSourceRawValue.bookmaker),
      sport: new FormControl(eventSourceRawValue.sport),
      period: new FormControl(eventSourceRawValue.period),
      betType: new FormControl(eventSourceRawValue.betType),
    });
  }

  getEventSource(form: EventSourceFormGroup): IEventSource | NewEventSource {
    return this.convertEventSourceRawValueToEventSource(form.getRawValue() as EventSourceFormRawValue | NewEventSourceFormRawValue);
  }

  resetForm(form: EventSourceFormGroup, eventSource: EventSourceFormGroupInput): void {
    const eventSourceRawValue = this.convertEventSourceToEventSourceRawValue({ ...this.getFormDefaults(), ...eventSource });
    form.reset(
      {
        ...eventSourceRawValue,
        id: { value: eventSourceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EventSourceFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      koefLastModifiedAt: currentTime,
      scannedAt: currentTime,
      startedAt: currentTime,
      updatedAt: currentTime,
    };
  }

  private convertEventSourceRawValueToEventSource(
    rawEventSource: EventSourceFormRawValue | NewEventSourceFormRawValue,
  ): IEventSource | NewEventSource {
    return {
      ...rawEventSource,
      koefLastModifiedAt: dayjs(rawEventSource.koefLastModifiedAt, DATE_TIME_FORMAT),
      scannedAt: dayjs(rawEventSource.scannedAt, DATE_TIME_FORMAT),
      startedAt: dayjs(rawEventSource.startedAt, DATE_TIME_FORMAT),
      updatedAt: dayjs(rawEventSource.updatedAt, DATE_TIME_FORMAT),
    };
  }

  private convertEventSourceToEventSourceRawValue(
    eventSource: IEventSource | (Partial<NewEventSource> & EventSourceFormDefaults),
  ): EventSourceFormRawValue | PartialWithRequiredKeyOf<NewEventSourceFormRawValue> {
    return {
      ...eventSource,
      koefLastModifiedAt: eventSource.koefLastModifiedAt ? eventSource.koefLastModifiedAt.format(DATE_TIME_FORMAT) : undefined,
      scannedAt: eventSource.scannedAt ? eventSource.scannedAt.format(DATE_TIME_FORMAT) : undefined,
      startedAt: eventSource.startedAt ? eventSource.startedAt.format(DATE_TIME_FORMAT) : undefined,
      updatedAt: eventSource.updatedAt ? eventSource.updatedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
