import {inject, Injectable} from '@angular/core';
import {Router} from '@angular/router';

import {type BookSummary} from '../data/book-response.models';

@Injectable({providedIn: 'root'})
export class BookReadService {
  private readonly router = inject(Router);

  readBook(book: BookSummary): void {
    const baseUrl = this.readerRoute(book.primaryFile?.bookType);
    if (!baseUrl) {
      console.error('Unsupported book type:', book.primaryFile?.bookType);
      return;
    }

    void this.router.navigate([`/${baseUrl}/book/${book.id}`]);
  }

  private readerRoute(
    bookType: NonNullable<BookSummary['primaryFile']>['bookType'] | undefined,
  ): string | null {
    switch (bookType) {
      case 'PDF':
        return 'pdf-reader';
      case 'EPUB':
      case 'FB2':
      case 'MOBI':
      case 'AZW3':
        return 'ebook-reader';
      case 'CBX':
        return 'cbx-reader';
      case 'AUDIOBOOK':
        return 'audiobook-player';
      default:
        return null;
    }
  }
}
